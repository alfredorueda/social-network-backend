import { Moment } from 'moment';
import { IProfile } from 'app/shared/model/profile.model';

export interface IDirectMessage {
  id?: number;
  createdDate?: Moment;
  message?: string;
  url?: string;
  pictureContentType?: string;
  picture?: any;
  sender?: IProfile;
  recipient?: IProfile;
}

export const defaultValue: Readonly<IDirectMessage> = {};
