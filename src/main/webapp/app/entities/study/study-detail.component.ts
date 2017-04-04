import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Study } from './study.model';
import { StudyService } from './study.service';

@Component({
    selector: 'jhi-study-detail',
    templateUrl: './study-detail.component.html'
})
export class StudyDetailComponent implements OnInit, OnDestroy {

    study: Study;
    private subscription: any;

    constructor(
        private studyService: StudyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.studyService.find(id).subscribe(study => {
            this.study = study;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
