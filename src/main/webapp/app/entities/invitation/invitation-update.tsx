import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProfile } from 'app/shared/model/profile.model';
import { getEntities as getProfiles } from 'app/entities/profile/profile.reducer';
import { getEntity, updateEntity, createEntity, reset } from './invitation.reducer';
import { IInvitation } from 'app/shared/model/invitation.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IInvitationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IInvitationUpdateState {
  isNew: boolean;
  sentId: string;
  receivedId: string;
}

export class InvitationUpdate extends React.Component<IInvitationUpdateProps, IInvitationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      sentId: '0',
      receivedId: '0',
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
      const { invitationEntity } = this.props;
      const entity = {
        ...invitationEntity,
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
    this.props.history.push('/entity/invitation');
  };

  render() {
    const { invitationEntity, profiles, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="socialNetworkBackendApp.invitation.home.createOrEditLabel">
              <Translate contentKey="socialNetworkBackendApp.invitation.home.createOrEditLabel">Create or edit a Invitation</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : invitationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="invitation-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="acceptedLabel" check>
                    <AvInput id="invitation-accepted" type="checkbox" className="form-control" name="accepted" />
                    <Translate contentKey="socialNetworkBackendApp.invitation.accepted">Accepted</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="sent.login">
                    <Translate contentKey="socialNetworkBackendApp.invitation.sent">Sent</Translate>
                  </Label>
                  <AvInput id="invitation-sent" type="select" className="form-control" name="sent.id">
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
                  <Label for="received.login">
                    <Translate contentKey="socialNetworkBackendApp.invitation.received">Received</Translate>
                  </Label>
                  <AvInput id="invitation-received" type="select" className="form-control" name="received.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/invitation" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
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
  invitationEntity: storeState.invitation.entity,
  loading: storeState.invitation.loading,
  updating: storeState.invitation.updating,
  updateSuccess: storeState.invitation.updateSuccess
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
)(InvitationUpdate);
