/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { QualOptTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ParticipantDetailComponent } from '../../../../../../main/webapp/app/entities/participant/participant-detail.component';
import { ParticipantService } from '../../../../../../main/webapp/app/entities/participant/participant.service';
import { Participant } from '../../../../../../main/webapp/app/entities/participant/participant.model';

describe('Component Tests', () => {

    describe('Participant Management Detail Component', () => {
        let comp: ParticipantDetailComponent;
        let fixture: ComponentFixture<ParticipantDetailComponent>;
        let service: ParticipantService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [QualOptTestModule],
                declarations: [ParticipantDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ParticipantService,
                    JhiEventManager
                ]
            }).overrideTemplate(ParticipantDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ParticipantDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParticipantService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Participant(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.participant).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
