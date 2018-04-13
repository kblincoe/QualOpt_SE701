import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Rx';
import { QualOptTestModule } from '../../../test.module';
import { SettingsComponent } from '../../../../../../main/webapp/app/account/settings/settings.component';
import { PasswordService } from '../../../../../../main/webapp/app/account/password/password.service';
import { Principal } from '../../../../../../main/webapp/app/shared/auth/principal.service';
import { AccountService } from '../../../../../../main/webapp/app/shared/auth/account.service';
import {LoginService} from '../../../../../../main/webapp/app/shared/login/login.service';
import { AuthServerProvider } from '../../../../../../main/webapp/app/shared/auth/auth-session.service';

describe('Component Tests', () => {

    describe('PasswordComponent', () => {

        let comp: SettingsComponent;
        let fixture: ComponentFixture<SettingsComponent>;
        let service: PasswordService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QualOptTestModule],
                declarations: [SettingsComponent],
                providers: [
                    Principal,
                    AccountService,
                    PasswordService,
                    LoginService,
                    AuthServerProvider
                ]
            }).overrideTemplate(SettingsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SettingsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PasswordService);
        });

        it('should show passwordError if passwords do not match', () => {
            // GIVEN
            comp.password = 'password1';
            comp.confirmPassword = 'password2';
            // WHEN
            comp.changePassword();
            // THEN
            expect(comp.doNotMatch).toBe('ERROR');
            expect(comp.passwordError).toBeNull();
            expect(comp.passwordSuccess).toBeNull();
        });

        it('should call Auth.changePassword when passwords match', () => {
            // GIVEN
            spyOn(service, 'save').and.returnValue(Observable.of(true));
            comp.password = comp.confirmPassword = 'myPassword';

            // WHEN
            comp.changePassword();

            // THEN
            expect(service.save).toHaveBeenCalledWith('myPassword');
        });

        it('should set passwordSuccess to OK upon passwordSuccess', function() {
            // GIVEN
            spyOn(service, 'save').and.returnValue(Observable.of(true));
            comp.password = comp.confirmPassword = 'myPassword';

            // WHEN
            comp.changePassword();

            // THEN
            expect(comp.doNotMatch).toBeNull();
            expect(comp.passwordError).toBeNull();
            expect(comp.passwordSuccess).toBe('OK');
        });

        it('should notify of passwordError if change password fails', function() {
            // GIVEN
            spyOn(service, 'save').and.returnValue(Observable.throw('ERROR'));
            comp.password = comp.confirmPassword = 'myPassword';

            // WHEN
            comp.changePassword();

            // THEN
            expect(comp.doNotMatch).toBeNull();
            expect(comp.passwordSuccess).toBeNull();
            expect(comp.passwordError).toBe('ERROR');
        });
    });
});
