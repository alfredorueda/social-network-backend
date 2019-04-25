import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './invitation.reducer';
import { IInvitation } from 'app/shared/model/invitation.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInvitationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class InvitationDetail extends React.Component<IInvitationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { invitationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="socialNetworkBackendApp.invitation.detail.title">Invitation</Translate> [<b>{invitationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="createdDate">
                <Translate contentKey="socialNetworkBackendApp.invitation.createdDate">Created Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={invitationEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="accepted">
                <Translate contentKey="socialNetworkBackendApp.invitation.accepted">Accepted</Translate>
              </span>
            </dt>
            <dd>{invitationEntity.accepted ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.invitation.sent">Sent</Translate>
            </dt>
            <dd>{invitationEntity.sent ? invitationEntity.sent.displayName : ''}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.invitation.received">Received</Translate>
            </dt>
            <dd>{invitationEntity.received ? invitationEntity.received.displayName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/invitation" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/invitation/${invitationEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ invitation }: IRootState) => ({
  invitationEntity: invitation.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InvitationDetail);
