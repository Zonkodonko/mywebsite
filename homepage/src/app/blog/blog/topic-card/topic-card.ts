import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Topic, TopicRaw} from '../../data/BlogTypes';
import {AuthenticationService} from '../../../authentication/authentication.service';

@Component({
  selector: 'app-topic-card',
  standalone: false,
  templateUrl: './topic-card.html',
  styleUrl: './topic-card.scss'
})
export class TopicCard {

  @Input() topic!: Topic;

  @Output() openTopic: EventEmitter<Topic> = new EventEmitter<Topic>();
  @Output() delete: EventEmitter<Topic> = new EventEmitter<Topic>();

  public isHovered: boolean = false;

  constructor(private authService: AuthenticationService) {

  }

  get isLoggedIn() {
    return this.authService.isLoggedIn;
  }

  open(event: Event) {
    event.stopPropagation();
    this.openTopic.emit(this.topic);
  }

  deleteTopic(event: Event) {
    event.stopPropagation();
    this.delete.emit(this.topic);
  }


}
