import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager , JhiDataUtils } from 'ng-jhipster';

import { Email } from './email.model';
import { EmailService } from './email.service';

@Component({
    selector: 'jhi-email-detail',
    templateUrl: './email-detail.component.html'
})
export class EmailDetailComponent implements OnInit, OnDestroy {

    email: Email;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private emailService: EmailService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEmails();
    }

    load(id) {
        this.emailService.find(id).subscribe((email) => {
            this.email = email;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEmails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'emailListModification',
            (response) => this.load(this.email.id)
        );
    }
}
