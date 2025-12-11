import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationService} from '../../../authentication/authentication.service';
import {NewTopic, Topic, TopicRaw} from '../../data/BlogTypes';
import {TranslateService} from '@ngx-translate/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {TopicDialog} from '../../topic-dialog/topic-dialog';
import {TopicApi} from '../../services/topic-service/topic-api';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-topic-overview-component',
  standalone: false,
  templateUrl: './topic-overview.component.html',
  styleUrl: './topic-overview.component.scss'
})
export class TopicOverview implements OnInit, OnDestroy {

  private _topicsRaw: TopicRaw[] = [];
  private _topics: Topic[] = [];

  private creating?: TopicRaw;
  private languageSubscription!: Subscription;

  constructor(private authService: AuthenticationService,
              private topicService: TopicApi,
              private langService: TranslateService,
              private modalService: NgbModal) {

    this.languageSubscription = langService.onLangChange.subscribe(() => {
      this.updateTopics();
    })
  }

  ngOnInit(): void {
    this.topicService.getTopics().subscribe(topics => {
      this._topicsRaw = topics
      this._topics = topics.map(t => this.mapTopic(t));
    });
  }

  ngOnDestroy(): void {
    this.languageSubscription.unsubscribe();
  }

  isLoggedIn() {
    return this.authService.isLoggedIn;
  }

  get topics() {
    return this._topics;
  }

  private updateTopics() {
    this._topics = this._topicsRaw.map(t => this.mapTopic(t));
  }

  /**
   * Sets correct translation for title and description of topic.
   * @param raw topic data from backend
   */
  private mapTopic(raw: TopicRaw): Topic {
    let lang = this.langService.getCurrentLang();
    return {...raw, ...{title: raw.title[lang], description: raw.description[lang]}}
  }

  openTopicDialog(id?: string) {
    let ngbModalRef = this.modalService.open(TopicDialog, {centered: true, size: 'lg', backdrop: 'static'});
    if (id !== undefined) {
      let topic = this._topicsRaw.find(e => e.id === id)!;
      ngbModalRef.componentInstance.setData(topic);
    } else if (this.creating !== undefined) {
      ngbModalRef.componentInstance.setData(this.creating);
    }

    ngbModalRef.closed.subscribe((result: NewTopic) => {
      this.topicService.createTopic(result).subscribe(
        () => {
          const existingTopicI = this._topics.findIndex(t => t.id === result.id);
          if (existingTopicI === -1) {
            this._topicsRaw.push(result);
          } else {
            this._topicsRaw[existingTopicI] = {...result, image: URL.createObjectURL(result.image)};
          }
          this.creating = undefined;
          this.updateTopics();
        },
        (error) => {
          console.error("Failed to create topic", error);
          this.creating = result;

        }
      );

    })

  }

}
