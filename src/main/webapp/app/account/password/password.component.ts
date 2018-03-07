import { Component, OnInit } from '@angular/core';

import { Principal } from '../../shared';
import { PasswordService } from './password.service';
import { Account } from '../../shared';
import { LoginService } from "../../shared/login/login.service"; 
@Component({
    selector: 'jhi-password',
    templateUrl: './password.component.html'
})
export class PasswordComponent implements OnInit {
    doNotMatch: string;
    error: string;
    success: string;
    account: Account;
    password: string;
    currentPassword: string; 
    confirmPassword: string;
    wrongPassword  : boolean; 
    constructor(
        private loginService: LoginService,
        private passwordService: PasswordService,
        private principal: Principal
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
    }

    changePassword() {
        // Check if we can vaidate the entered password.
        this.loginService.login({
            username: this.account.login,
            password: this.currentPassword,
            rememberMe: this.loginService.getRememberMe()
        }).then(() => {
            // check if new passwords match 
            if (this.password !== this.confirmPassword) {
                this.error = null;
                this.success = null;
                this.doNotMatch = 'ERROR';
            } 
            //update password
            else{
                this.wrongPassword = false;
                this.doNotMatch = null;
                this.passwordService.save(this.password).subscribe(() => {
                    this.error = null;
                    this.success = 'OK';
                }, () => {
                    this.success = null;
                    this.error = 'ERROR';
                });
            }
        // if password is invalid then throw error message
        }).catch(() => {
            this.success = null;
            this.wrongPassword = true; 
        });

    }

}
