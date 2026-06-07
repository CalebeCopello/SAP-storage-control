import { Routes } from '@angular/router';
import { UserRegister } from './features/auth/user-register/user-register';

export const routes: Routes = [
    {
        path: 'user/register',
        component: UserRegister
    }
];
