import { Injectable } from '@angular/core';

import { Principal } from '../auth/principal.service';
import { AuthServerProvider } from '../auth/auth-session.service';

@Injectable()
export class LoginService {
private rememberMe;
private username; 
    constructor(
        private principal: Principal,
        private authServerProvider: AuthServerProvider
    ) {}


    login(credentials, callback?) {
        const cb = callback || function() {};

        return new Promise((resolve, reject) => {
            this.authServerProvider.login(credentials).subscribe((data) => {
                this.principal.identity(true).then((account) => {
                    resolve(data);
                });
                this.setRememberMe(credentials.rememberMe);
                return cb();
            }, (err) => {
                this.logout();
                reject(err);reject(err);
                return cb(err);
            });
        });
    }

    setRememberMe(rememberMe){
        this.rememberMe = rememberMe; 
    }

    getRememberMe() : boolean {
        return this.rememberMe; 
    }
    logout() {
        this.authServerProvider.logout().subscribe();
        this.principal.authenticate(null);
    }
}
