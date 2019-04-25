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
import { IProfile } from 'app/shared/model/profile.model';
import { getEntities as getProfiles } from 'app/entities/profile/profile.reducer';
import { IChatroom } from 'app/shared/model/chatroom.model';
import { getEntities as getChatrooms } from 'app/entities/chatroom/chatroom.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './message.reducer';
import { IMessage } from 'app/shared/model/message.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMessageUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMessageUpdateState {
  isNew: boolean;
  locationId: string;
  senderId: string;
  chatroomId: string;
}

export class MessageUpdate extends React.Component<IMessageUpdateProps, IMessageUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      locationId: '0',
      senderId: '0',
      chatroomId: '0',
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
    this.props.getProfiles();
    this.props.getChatrooms();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.createdDate = convertDateTimeToServer(values.createdDate);

    if (errors.length === 0) {
      const { messageEntity } = this.props;
      const entity = {
        ...messageEntity,
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
    this.props.history.push('/entity/message');
  };

  render() {
    const { messageEntity, locations, profiles, chatrooms, loading, updating } = this.props;
    const { isNew } = this.state;

    const { picture, pictureContentType } = messageEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="socialNetworkBackendApp.message.home.createOrEditLabel">
              <Translate contentKey="socialNetworkBackendApp.message.home.createOrEditLabel">Create or edit a Message</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : messageEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="message-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="createdDateLabel" for="createdDate">
                    <Translate contentKey="socialNetworkBackendApp.message.createdDate">Created Date</Translate>
                  </Label>
                  <AvInput
                    id="message-createdDate"
                    type="datetime-local"
                    className="form-control"
                    name="createdDate"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.messageEntity.createdDate)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="messageLabel" for="message">
                    <Translate contentKey="socialNetworkBackendApp.message.message">Message</Translate>
                  </Label>
                  <AvField id="message-message" type="text" name="message" />
                </AvGroup>
                <AvGroup>
                  <Label id="urlLabel" for="url">
                    <Translate contentKey="socialNetworkBackendApp.message.url">Url</Translate>
                  </Label>
                  <AvField id="message-url" type="text" name="url" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="pictureLabel" for="picture">
                      <Translate contentKey="socialNetworkBackendApp.message.picture">Picture</Translate>
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
                  <Label for="location.address">
                    <Translate contentKey="socialNetworkBackendApp.message.location">Location</Translate>
                  </Label>
                  <AvInput id="message-location" type="select" className="form-control" name="location.id">
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
                  <Label for="sender.displayName">
                    <Translate contentKey="socialNetworkBackendApp.message.sender">Sender</Translate>
                  </Label>
                  <AvInput id="message-sender" type="select" className="form-control" name="sender.id">
                    <option value="" key="0" />
                    {profiles
                      ? profiles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.displayName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="chatroom.topic">
                    <Translate contentKey="socialNetworkBackendApp.message.chatroom">Chatroom</Translate>
                  </Label>
                  <AvInput id="message-chatroom" type="select" className="form-control" name="chatroom.id">
                    <option value="" key="0" />
                    {chatrooms
                      ? chatrooms.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.topic}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/message" replace color="info">
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
  profiles: storeState.profile.entities,
  chatrooms: storeState.chatroom.entities,
  messageEntity: storeState.message.entity,
  loading: storeState.message.loading,
  updating: storeState.message.updating,
  updateSuccess: storeState.message.updateSuccess
});

const mapDispatchToProps = {
  getLocations,
  getProfiles,
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
)(MessageUpdate);
