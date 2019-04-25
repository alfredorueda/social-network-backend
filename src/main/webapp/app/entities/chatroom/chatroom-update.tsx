import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProfile } from 'app/shared/model/profile.model';
import { getEntities as getProfiles } from 'app/entities/profile/profile.reducer';
import { getEntity, updateEntity, createEntity, reset } from './chatroom.reducer';
import { IChatroom } from 'app/shared/model/chatroom.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IChatroomUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IChatroomUpdateState {
  isNew: boolean;
  idsparticipant: any[];
  adminId: string;
}

export class ChatroomUpdate extends React.Component<IChatroomUpdateProps, IChatroomUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsparticipant: [],
      adminId: '0',
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

  saveEntity = (event, errors, values) => {
    values.createdDate = convertDateTimeToServer(values.createdDate);

    if (errors.length === 0) {
      const { chatroomEntity } = this.props;
      const entity = {
        ...chatroomEntity,
        ...values,
        participants: mapIdList(values.participants)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/chatroom');
  };

  render() {
    const { chatroomEntity, profiles, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="socialNetworkBackendApp.chatroom.home.createOrEditLabel">
              <Translate contentKey="socialNetworkBackendApp.chatroom.home.createOrEditLabel">Create or edit a Chatroom</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : chatroomEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="chatroom-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="createdDateLabel" for="createdDate">
                    <Translate contentKey="socialNetworkBackendApp.chatroom.createdDate">Created Date</Translate>
                  </Label>
                  <AvInput
                    id="chatroom-createdDate"
                    type="datetime-local"
                    className="form-control"
                    name="createdDate"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.chatroomEntity.createdDate)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="topicLabel" for="topic">
                    <Translate contentKey="socialNetworkBackendApp.chatroom.topic">Topic</Translate>
                  </Label>
                  <AvField
                    id="chatroom-topic"
                    type="text"
                    name="topic"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="admin.displayName">
                    <Translate contentKey="socialNetworkBackendApp.chatroom.admin">Admin</Translate>
                  </Label>
                  <AvInput id="chatroom-admin" type="select" className="form-control" name="admin.id">
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
                  <Label for="profiles">
                    <Translate contentKey="socialNetworkBackendApp.chatroom.participant">Participant</Translate>
                  </Label>
                  <AvInput
                    id="chatroom-participant"
                    type="select"
                    multiple
                    className="form-control"
                    name="participants"
                    value={chatroomEntity.participants && chatroomEntity.participants.map(e => e.id)}
                  >
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
                <Button tag={Link} id="cancel-save" to="/entity/chatroom" replace color="info">
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
  chatroomEntity: storeState.chatroom.entity,
  loading: storeState.chatroom.loading,
  updating: storeState.chatroom.updating,
  updateSuccess: storeState.chatroom.updateSuccess
});

const mapDispatchToProps = {
  getProfiles,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ChatroomUpdate);
