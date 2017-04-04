import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ResearcherDetailComponent } from '../../../../../../main/webapp/app/entities/researcher/researcher-detail.component';
import { ResearcherService } from '../../../../../../main/webapp/app/entities/researcher/researcher.service';
import { Researcher } from '../../../../../../main/webapp/app/entities/researcher/researcher.model';

describe('Component Tests', () => {

    describe('Researcher Management Detail Component', () => {
        let comp: ResearcherDetailComponent;
        let fixture: ComponentFixture<ResearcherDetailComponent>;
        let service: ResearcherService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [ResearcherDetailComponent],
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
                    ResearcherService
                ]
            }).overrideComponent(ResearcherDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ResearcherDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ResearcherService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Researcher(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.researcher).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
