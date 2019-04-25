import { Moment } from 'moment';
import { ILocation } from 'app/shared/model/location.model';
import { IProfile } from 'app/shared/model/profile.model';
import { IChatroom } from 'app/shared/model/chatroom.model';

export interface IMessage {
  id?: number;
  createdDate?: Moment;
  message?: string;
  url?: string;
  pictureContentType?: string;
  picture?: any;
  location?: ILocation;
  sender?: IProfile;
  chatroom?: IChatroom;
}

export const defaultValue: Readonly<IMessage> = {};
