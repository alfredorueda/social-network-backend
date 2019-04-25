import { IProfile } from 'app/shared/model/profile.model';
import { IMessage } from 'app/shared/model/message.model';

export interface ILocation {
  id?: number;
  latitude?: number;
  longitude?: number;
  urlGoogleMaps?: string;
  urlOpenStreetMap?: string;
  address?: string;
  postalCode?: string;
  city?: string;
  stateProvice?: string;
  county?: string;
  country?: string;
  user?: IProfile;
  message?: IMessage;
}

export const defaultValue: Readonly<ILocation> = {};
