import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { StudyInfo } from './study.model';
import { StudyService } from './study.service';
import { Document } from '../document';
import { DocumentService } from '../document/document.service';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-study-info',
    templateUrl: './study-info.component.html'
})
export class StudyInfoComponent implements OnInit {

    private subscription: Subscription;
    studyInfo: StudyInfo;

    constructor(
        private studyService: StudyService,
        private eventManager: JhiEventManager,
        private route: ActivatedRoute,
        private documentService: DocumentService
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
    }

    load(id: number) {
        this.studyService.getStudyInfo(id).subscribe((studyInfo) => {
            this.studyInfo = studyInfo;
        });
    }

    getFileExtension(filename: string) {
        return filename.split('.').pop();
    }

    downloadFileUrl(id: number) {
        return this.documentService.downloadUrl(id);
    }
}