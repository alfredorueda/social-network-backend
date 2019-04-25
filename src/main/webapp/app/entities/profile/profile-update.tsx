import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IRelationship } from 'app/shared/model/relationship.model';
import { getEntities as getRelationships } from 'app/entities/relationship/relationship.reducer';
import { IGender } from 'app/shared/model/gender.model';
import { getEntities as getGenders } from 'app/entities/gender/gender.reducer';
import { IEthnicity } from 'app/shared/model/ethnicity.model';
import { getEntities as getEthnicities } from 'app/entities/ethnicity/ethnicity.reducer';
import { IChatroom } from 'app/shared/model/chatroom.model';
import { getEntities as getChatrooms } from 'app/entities/chatroom/chatroom.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './profile.reducer';
import { IProfile } from 'app/shared/model/profile.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProfileUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IProfileUpdateState {
  isNew: boolean;
  locationId: string;
  userId: string;
  relationshipId: string;
  genderId: string;
  ethnicityId: string;
  adminChatroomId: string;
  joinedChatroomId: string;
}

export class ProfileUpdate extends React.Component<IProfileUpdateProps, IProfileUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      locationId: '0',
      userId: '0',
      relationshipId: '0',
      genderId: '0',
      ethnicityId: '0',
      adminChatroomId: '0',
      joinedChatroomId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getLocations();
    this.props.getUsers();
    this.props.getRelationships();
    this.props.getGenders();
    this.props.getEthnicities();
    this.props.getChatrooms();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { profileEntity } = this.props;
      const entity = {
        ...profileEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/profile');
  };

  render() {
    const { profileEntity, locations, users, relationships, genders, ethnicities, chatrooms, loading, updating } = this.props;
    const { isNew } = this.state;

    const { picture, pictureContentType } = profileEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="socialNetworkBackendApp.profile.home.createOrEditLabel">
              <Translate contentKey="socialNetworkBackendApp.profile.home.createOrEditLabel">Create or edit a Profile</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : profileEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="profile-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="birthDateLabel" for="birthDate">
                    <Translate contentKey="socialNetworkBackendApp.profile.birthDate">Birth Date</Translate>
                  </Label>
                  <AvField id="profile-birthDate" type="date" className="form-control" name="birthDate" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="pictureLabel" for="picture">
                      <Translate contentKey="socialNetworkBackendApp.profile.picture">Picture</Translate>
                    </Label>
                    <br />
                    {picture ? (
                      <div>
                        <a onClick={openFile(pictureContentType, picture)}>
                          <img src={`data:${pictureContentType};base64,${picture}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {pictureContentType}, {byteSize(picture)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('picture')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_picture" type="file" onChange={this.onBlobChange(true, 'picture')} accept="image/*" />
                    <AvInput type="hidden" name="picture" value={picture} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="heightLabel" for="height">
                    <Translate contentKey="socialNetworkBackendApp.profile.height">Height</Translate>
                  </Label>
                  <AvField id="profile-height" type="string" className="form-control" name="height" />
                </AvGroup>
                <AvGroup>
                  <Label id="weightLabel" for="weight">
                    <Translate contentKey="socialNetworkBackendApp.profile.weight">Weight</Translate>
                  </Label>
                  <AvField id="profile-weight" type="string" className="form-control" name="weight" />
                </AvGroup>
                <AvGroup>
                  <Label id="unitSystemLabel">
                    <Translate contentKey="socialNetworkBackendApp.profile.unitSystem">Unit System</Translate>
                  </Label>
                  <AvInput
                    id="profile-unitSystem"
                    type="select"
                    className="form-control"
                    name="unitSystem"
                    value={(!isNew && profileEntity.unitSystem) || 'IMPERIAL'}
                  >
                    <option value="IMPERIAL">
                      <Translate contentKey="socialNetworkBackendApp.UnitSystem.IMPERIAL" />
                    </option>
                    <option value="METRIC">
                      <Translate contentKey="socialNetworkBackendApp.UnitSystem.METRIC" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="aboutMeLabel" for="aboutMe">
                    <Translate contentKey="socialNetworkBackendApp.profile.aboutMe">About Me</Translate>
                  </Label>
                  <AvField id="profile-aboutMe" type="text" name="aboutMe" />
                </AvGroup>
                <AvGroup>
                  <Label id="displayNameLabel" for="displayName">
                    <Translate contentKey="socialNetworkBackendApp.profile.displayName">Display Name</Translate>
                  </Label>
                  <AvField
                    id="profile-displayName"
                    type="text"
                    name="displayName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="showAgeLabel" check>
                    <AvInput id="profile-showAge" type="checkbox" className="form-control" name="showAge" />
                    <Translate contentKey="socialNetworkBackendApp.profile.showAge">Show Age</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="bannedLabel" check>
                    <AvInput id="profile-banned" type="checkbox" className="form-control" name="banned" />
                    <Translate contentKey="socialNetworkBackendApp.profile.banned">Banned</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="filterPreferencesLabel" for="filterPreferences">
                    <Translate contentKey="socialNetworkBackendApp.profile.filterPreferences">Filter Preferences</Translate>
                  </Label>
                  <AvField id="profile-filterPreferences" type="text" name="filterPreferences" />
                </AvGroup>
                <AvGroup>
                  <Label for="location.address">
                    <Translate contentKey="socialNetworkBackendApp.profile.location">Location</Translate>
                  </Label>
                  <AvInput id="profile-location" type="select" className="form-control" name="location.id">
                    <option value="" key="0" />
                    {locations
                      ? locations.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.address}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="user.login">
                    <Translate contentKey="socialNetworkBackendApp.profile.user">User</Translate>
                  </Label>
                  <AvInput id="profile-user" type="select" className="form-control" name="user.id">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="relationship.status">
                    <Translate contentKey="socialNetworkBackendApp.profile.relationship">Relationship</Translate>
                  </Label>
                  <AvInput id="profile-relationship" type="select" className="form-control" name="relationship.id">
                    <option value="" key="0" />
                    {relationships
                      ? relationships.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.status}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="gender.type">
                    <Translate contentKey="socialNetworkBackendApp.profile.gender">Gender</Translate>
                  </Label>
                  <AvInput id="profile-gender" type="select" className="form-control" name="gender.id">
                    <option value="" key="0" />
                    {genders
                      ? genders.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.type}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="ethnicity.ethnicity">
                    <Translate contentKey="socialNetworkBackendApp.profile.ethnicity">Ethnicity</Translate>
                  </Label>
                  <AvInput id="profile-ethnicity" type="select" className="form-control" name="ethnicity.id">
                    <option value="" key="0" />
                    {ethnicities
                      ? ethnicities.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.ethnicity}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/profile" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  locations: storeState.location.entities,
  users: storeState.userManagement.users,
  relationships: storeState.relationship.entities,
  genders: storeState.gender.entities,
  ethnicities: storeState.ethnicity.entities,
  chatrooms: storeState.chatroom.entities,
  profileEntity: storeState.profile.entity,
  loading: storeState.profile.loading,
  updating: storeState.profile.updating,
  updateSuccess: storeState.profile.updateSuccess
});

const mapDispatchToProps = {
  getLocations,
  getUsers,
  getRelationships,
  getGenders,
  getEthnicities,
  getChatrooms,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProfileUpdate);
