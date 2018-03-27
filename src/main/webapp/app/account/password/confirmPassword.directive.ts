import { Directive, forwardRef, Attribute } from '@angular/core';
import { Validator, AbstractControl, NG_VALIDATORS } from '@angular/forms';

@Directive({
    selector: '[validateEqual][formControlName],[validateEqual][formControl],[validateEqual][ngModel]',
    providers: [
        { 
            provide: NG_VALIDATORS, 
            useExisting: forwardRef(() => EqualValidator), 
            multi: true 
        }
    ]
})
export class EqualValidator implements Validator {
    constructor(@Attribute('validateEqual') public validateEqual: string,
    @Attribute('editingNew') public editingNew: string) {
    }

    private get isEditingNew() {
        return this.editingNew === 'true';
    }

    validate(control: AbstractControl): { [key: string]: any } {
        // self/other is a pair of new/confirm. When new is self, then other is confirm. Vice versa.
        let selfValue = control.value;
        let otherControl = control.root.get(this.validateEqual);

        // case when editing the confirm field
        if (otherControl && selfValue !== otherControl.value && !this.isEditingNew) {
            return {
                validateEqual: false
            }
        }

        // case when editing the new field and confirm is the same
        if (otherControl && selfValue === otherControl.value && this.isEditingNew) {
            delete otherControl.errors['validateEqual'];
            if (!Object.keys(otherControl.errors).length) otherControl.setErrors(null);
        }

        // case when editing the new field and confirm is not the same
        if (otherControl && selfValue !== otherControl.value && this.isEditingNew) {
            otherControl.setErrors({ validateEqual: false });
        }

        return null;
    }
}