import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Researcher } from './researcher.model';
import { ResearcherService } from './researcher.service';

@Component({
    selector: 'jhi-researcher-detail',
    templateUrl: './researcher-detail.component.html'
})
export class ResearcherDetailComponent implements OnInit, OnDestroy {

    researcher: Researcher;
    private subscription: any;

    constructor(
        private researcherService: ResearcherService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.researcherService.find(id).subscribe(researcher => {
            this.researcher = researcher;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
