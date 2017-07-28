import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Researcher } from './researcher.model';
import { ResearcherService } from './researcher.service';

@Component({
    selector: 'jhi-researcher-detail',
    templateUrl: './researcher-detail.component.html'
})
export class ResearcherDetailComponent implements OnInit, OnDestroy {

    researcher: Researcher;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private researcherService: ResearcherService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInResearchers();
    }

    load(id) {
        this.researcherService.find(id).subscribe((researcher) => {
            this.researcher = researcher;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInResearchers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'researcherListModification',
            (response) => this.load(this.researcher.id)
        );
    }
}
