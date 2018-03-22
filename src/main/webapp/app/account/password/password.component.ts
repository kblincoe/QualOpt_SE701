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
    errorBlock1 : boolean;
    errorBlock2 : boolean;
    errorBlock3 : boolean;
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

    // Set all input fields to null to remove previous input.
    clearInputFields() {
        this.currentPassword = null;
        this.password = null;
        this.confirmPassword = null;
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
                    this.clearInputFields();
                    this.errorBlock1 = true; 
                    this.errorBlock2 = true; 
                    this.errorBlock3 = true; 
                }, () => {
                    this.success = null;
                    this.error = 'ERROR';
                });
            }
        // if password is invalid then throw error message
        }).catch(() => {
            this.success = null;
            this.wrongPassword = true;
            this.clearInputFields();
            this.errorBlock1 = true; 
            this.errorBlock2 = true; 
            this.errorBlock3 = true; 
        });
    }

    // re-enables the mandatory input error message from showing for the input fields
    unblockErrors(i){
        switch(i){
            case 1:
                if(this.currentPassword.length!=null){
                    this.errorBlock1 = false; 
                }
                break;
            case 2:
                if(this.password.length!=null){
                    this.errorBlock2 = false; 
                }
                break; 
            case 3:
                if(this.confirmPassword.length!=null){
                    this.errorBlock3 = false; 
                }
        }
    }
}
