import { Moment } from 'moment';
import { ILocation } from 'app/shared/model/location.model';
import { IUser } from 'app/shared/model/user.model';
import { IRelationship } from 'app/shared/model/relationship.model';
import { IGender } from 'app/shared/model/gender.model';
import { IEthnicity } from 'app/shared/model/ethnicity.model';
import { IInvitation } from 'app/shared/model/invitation.model';
import { IBlock } from 'app/shared/model/block.model';
import { IMessage } from 'app/shared/model/message.model';
import { IChatroom } from 'app/shared/model/chatroom.model';

export const enum UnitSystem {
  IMPERIAL = 'IMPERIAL',
  METRIC = 'METRIC'
}

export interface IProfile {
  id?: number;
  birthDate?: Moment;
  pictureContentType?: string;
  picture?: any;
  height?: number;
  weight?: number;
  unitSystem?: UnitSystem;
  aboutMe?: string;
  displayName?: string;
  showAge?: boolean;
  banned?: boolean;
  filterPreferences?: string;
  location?: ILocation;
  user?: IUser;
  relationship?: IRelationship;
  gender?: IGender;
  ethnicity?: IEthnicity;
  sentInvitations?: IInvitation[];
  receivedInvitations?: IInvitation[];
  sentBlocks?: IBlock[];
  receivedBlocks?: IBlock[];
  sentMessages?: IMessage[];
  adminChatrooms?: IChatroom[];
  joinedChatrooms?: IChatroom[];
}

export const defaultValue: Readonly<IProfile> = {
  showAge: false,
  banned: false
};
