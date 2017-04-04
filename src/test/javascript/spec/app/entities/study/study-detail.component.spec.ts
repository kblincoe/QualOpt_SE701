import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StudyDetailComponent } from '../../../../../../main/webapp/app/entities/study/study-detail.component';
import { StudyService } from '../../../../../../main/webapp/app/entities/study/study.service';
import { Study } from '../../../../../../main/webapp/app/entities/study/study.model';

describe('Component Tests', () => {

    describe('Study Management Detail Component', () => {
        let comp: StudyDetailComponent;
        let fixture: ComponentFixture<StudyDetailComponent>;
        let service: StudyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [StudyDetailComponent],
                providers: [
                    MockBackend,
                    BaseRequestOptions,
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    {
                        provide: Http,
                        useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
                            return new Http(backendInstance, defaultOptions);
                        },
                        deps: [MockBackend, BaseRequestOptions]
                    },
                    StudyService
                ]
            }).overrideComponent(StudyDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StudyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudyService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Study(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.study).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
