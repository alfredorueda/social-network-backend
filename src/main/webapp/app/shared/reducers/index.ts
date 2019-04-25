import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import profile, {
  ProfileState
} from 'app/entities/profile/profile.reducer';
// prettier-ignore
import gender, {
  GenderState
} from 'app/entities/gender/gender.reducer';
// prettier-ignore
import ethnicity, {
  EthnicityState
} from 'app/entities/ethnicity/ethnicity.reducer';
// prettier-ignore
import relationship, {
  RelationshipState
} from 'app/entities/relationship/relationship.reducer';
// prettier-ignore
import location, {
  LocationState
} from 'app/entities/location/location.reducer';
// prettier-ignore
import invitation, {
  InvitationState
} from 'app/entities/invitation/invitation.reducer';
// prettier-ignore
import block, {
  BlockState
} from 'app/entities/block/block.reducer';
// prettier-ignore
import chatroom, {
  ChatroomState
} from 'app/entities/chatroom/chatroom.reducer';
// prettier-ignore
import message, {
  MessageState
} from 'app/entities/message/message.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly profile: ProfileState;
  readonly gender: GenderState;
  readonly ethnicity: EthnicityState;
  readonly relationship: RelationshipState;
  readonly location: LocationState;
  readonly invitation: InvitationState;
  readonly block: BlockState;
  readonly chatroom: ChatroomState;
  readonly message: MessageState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  profile,
  gender,
  ethnicity,
  relationship,
  location,
  invitation,
  block,
  chatroom,
  message,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
