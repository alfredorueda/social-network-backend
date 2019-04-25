import { Moment } from 'moment';
import { IProfile } from 'app/shared/model/profile.model';
import { IMessage } from 'app/shared/model/message.model';

export interface IChatroom {
  id?: number;
  createdDate?: Moment;
  topic?: string;
  admin?: IProfile;
  participants?: IProfile[];
  messages?: IMessage[];
}

export const defaultValue: Readonly<IChatroom> = {};
