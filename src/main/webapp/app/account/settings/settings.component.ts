import { Component, OnInit, OnChanges } from '@angular/core';

import { Principal, AccountService } from '../../shared';

@Component({
    selector: 'jhi-settings',
    templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit, OnChanges {
    error: string;
    success: string;
    settingsAccount: any;
    languages: any[];
    email: string;
    constructor(
        private account: AccountService,
        private principal: Principal
    ) {
    }

    ngOnChanges() {
        let x = this.email.split("@");
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.settingsAccount = this.copyAccount(account);
        });
    }

    save() {
        this.account.save(this.settingsAccount).subscribe(() => {
            this.error = null;
            this.success = 'OK';
            this.principal.identity(true).then((account) => {
                this.settingsAccount = this.copyAccount(account);
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
