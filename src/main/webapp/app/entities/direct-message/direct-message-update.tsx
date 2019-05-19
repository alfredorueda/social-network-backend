import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProfile } from 'app/shared/model/profile.model';
import { getEntities as getProfiles } from 'app/entities/profile/profile.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './direct-message.reducer';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDirectMessageUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IDirectMessageUpdateState {
  isNew: boolean;
  senderId: string;
  recipientId: string;
}

export class DirectMessageUpdate extends React.Component<IDirectMessageUpdateProps, IDirectMessageUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      senderId: '0',
      recipientId: '0',
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

    this.props.getProfiles();
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
      const { directMessageEntity } = this.props;
      const entity = {
        ...directMessageEntity,
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
    this.props.history.push('/entity/direct-message');
  };

  render() {
    const { directMessageEntity, profiles, loading, updating } = this.props;
    const { isNew } = this.state;

    const { picture, pictureContentType } = directMessageEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="socialNetworkBackendApp.directMessage.home.createOrEditLabel">
              <Translate contentKey="socialNetworkBackendApp.directMessage.home.createOrEditLabel">
                Create or edit a DirectMessage
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : directMessageEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="direct-message-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="createdDateLabel" for="createdDate">
                    <Translate contentKey="socialNetworkBackendApp.directMessage.createdDate">Created Date</Translate>
                  </Label>
                  <AvInput
                    id="direct-message-createdDate"
                    type="datetime-local"
                    className="form-control"
                    name="createdDate"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.directMessageEntity.createdDate)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="messageLabel" for="message">
                    <Translate contentKey="socialNetworkBackendApp.directMessage.message">Message</Translate>
                  </Label>
                  <AvField id="direct-message-message" type="text" name="message" />
                </AvGroup>
                <AvGroup>
                  <Label id="urlLabel" for="url">
                    <Translate contentKey="socialNetworkBackendApp.directMessage.url">Url</Translate>
                  </Label>
                  <AvField id="direct-message-url" type="text" name="url" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="pictureLabel" for="picture">
                      <Translate contentKey="socialNetworkBackendApp.directMessage.picture">Picture</Translate>
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
                  <Label for="sender.displayName">
                    <Translate contentKey="socialNetworkBackendApp.directMessage.sender">Sender</Translate>
                  </Label>
                  <AvInput id="direct-message-sender" type="select" className="form-control" name="sender.id">
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
                  <Label for="recipient.displayName">
                    <Translate contentKey="socialNetworkBackendApp.directMessage.recipient">Recipient</Translate>
                  </Label>
                  <AvInput id="direct-message-recipient" type="select" className="form-control" name="recipient.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/direct-message" replace color="info">
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
  profiles: storeState.profile.entities,
  directMessageEntity: storeState.directMessage.entity,
  loading: storeState.directMessage.loading,
  updating: storeState.directMessage.updating,
  updateSuccess: storeState.directMessage.updateSuccess
});

const mapDispatchToProps = {
  getProfiles,
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
)(DirectMessageUpdate);
