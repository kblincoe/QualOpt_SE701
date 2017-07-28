import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';

import { Researcher } from './researcher.model';
import { ResearcherService } from './researcher.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-researcher',
    templateUrl: './researcher.component.html'
})
export class ResearcherComponent implements OnInit, OnDestroy {
researchers: Researcher[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private researcherService: ResearcherService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.researcherService.query().subscribe(
            (res: ResponseWrapper) => {
                this.researchers = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInResearchers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Researcher) {
        return item.id;
    }
    registerChangeInResearchers() {
        this.eventSubscriber = this.eventManager.subscribe('researcherListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
