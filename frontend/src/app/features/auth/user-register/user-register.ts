import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../../environments/environment';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

interface UserRegisterResponse {
  id: string;
  name: string;
  email: string;
  createdAt: string;
  updatedAt: string;
  message: string;
  type: string;
}

@Component({
  selector: 'sc-user-register',
  imports: [
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    ReactiveFormsModule,
  ],
  templateUrl: './user-register.html',
  styleUrl: './user-register.scss',
})
export class UserRegister {
  registerForm: FormGroup;
  alerts: { type: string; message: string }[] = [];

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
  ) {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    this.alerts = [];
    this.registerForm.get('email')?.setErrors(null);

    this.http
      .post<UserRegisterResponse>(`${environment.apiUrl}/auth/register`, this.registerForm.value)
      .subscribe({
        next: (res: UserRegisterResponse) => {
          this.alerts = [{ type: res.type, message: res.message }];
          console.log('Success: ', res);
          setTimeout(() => {
            this.router.navigate(['/']);
            this.registerForm.reset();
          }, 3000);
        },
        error: (err: HttpErrorResponse) => {
          if (err.status === 409) {
            this.registerForm.get('email')?.setErrors({ emailInUse: true });
            console.error('Error: ', err);
          } else if (err.status === 422) {
            const fieldErrors: Record<string, string> = err.error?.errors ?? {};
            Object.entries(fieldErrors).forEach(([field, message]) => {
              this.registerForm.get(field)?.setErrors({ serverError: message });
              this.alerts.push({ type: 'danger', message });
              console.error('Error: ', err);
            });
          } else {
            this.alerts = [{ type: 'danger', message: 'Unexpected error. Please try again.' }];
            console.error('Error: ', err);
          }
        },
      });
  }
}
