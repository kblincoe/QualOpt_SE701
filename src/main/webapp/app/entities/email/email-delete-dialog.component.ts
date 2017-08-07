import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Email } from './email.model';
import { EmailPopupService } from './email-popup.service';
import { EmailService } from './email.service';

@Component({
    selector: 'jhi-email-delete-dialog',
    templateUrl: './email-delete-dialog.component.html'
})
export class EmailDeleteDialogComponent {

    email: Email;

    constructor(
        private emailService: EmailService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.emailService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'emailListModification',
                content: 'Deleted an email'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-email-delete-popup',
    template: ''
})
export class EmailDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private emailPopupService: EmailPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.emailPopupService
                .open(EmailDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
