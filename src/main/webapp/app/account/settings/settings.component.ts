import { Component, OnInit, OnChanges } from '@angular/core';
import {Principal, AccountService, Account} from '../../shared';
import {PasswordService} from '../password/password.service';
import {LoginService} from '../../shared/login/login.service';

@Component({
    selector: 'jhi-settings',
    templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit, OnChanges {
    fieldValues: { [key: string]: string; } = {};
    error: string;
    success: string;
    settingsAccount: any;
    languages: any[];
    email: string;
    doNotMatch: string;
    passwordError: string;
    passwordSuccess: string;
    account: Account;
    password: string;
    currentPassword: string;
    confirmPassword: string;
    wrongPassword: boolean;

    constructor(
        private accountService: AccountService,
        private principal: Principal,
        private loginService: LoginService,
        private passwordService: PasswordService
    ) {
    }

    ngOnChanges() {
        let x = this.email.split("@");
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.settingsAccount = this.copyAccount(account);
            this.initFieldValues(account);
            this.account = account;
        });
    }

    initFieldValues(account) {
        this.fieldValues['firstName'] = account.firstName;
        this.fieldValues['lastName'] = account.lastName;
        this.fieldValues['email'] = account.email;
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
                this.passwordError = null;
                this.passwordSuccess = null;
                this.doNotMatch = 'ERROR';
            } else { // update password
                this.wrongPassword = false;
                this.doNotMatch = null;
                this.passwordService.save(this.password).subscribe(() => {
                    this.passwordError = null;
                    this.passwordSuccess = 'OK';
                }, () => {
                    this.passwordSuccess = null;
                    this.passwordError = 'ERROR';
                });
            }
            // if password is invalid then throw error message
        }).catch(() => {
            this.passwordSuccess = null;
            this.wrongPassword = true;
        });

    }

    save() {
        this.accountService.save(this.settingsAccount).subscribe(() => {
            this.error = null;
            this.success = 'OK';
            this.principal.identity(true).then((account) => {
                this.settingsAccount = this.copyAccount(account);
                this.initFieldValues(account);
            });
        }, () => {
            this.success = null;
            this.error = 'ERROR';
        });
    }

    copyAccount(account) {
        return {
            activated: account.activated,
            email: account.email,
            firstName: account.firstName,
            langKey: account.langKey,
            lastName: account.lastName,
            login: account.login,
            imageUrl: account.imageUrl
        };
    }
}
