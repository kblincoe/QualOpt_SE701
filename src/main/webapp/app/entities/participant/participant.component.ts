import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';

import { Participant } from './participant.model';
import { ParticipantService } from './participant.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-participant',
    templateUrl: './participant.component.html'
})
export class ParticipantComponent implements OnInit, OnDestroy {
participants: Participant[];
filter: Participant = new Participant();
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private participantService: ParticipantService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.participantService.query().subscribe(
            (res: ResponseWrapper) => {
                this.participants = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInParticipants();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Participant) {
        return item.id;
    }
    registerChangeInParticipants() {
        this.eventSubscriber = this.eventManager.subscribe('participantListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
