import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './chatroom.reducer';
import { IChatroom } from 'app/shared/model/chatroom.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IChatroomDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ChatroomDetail extends React.Component<IChatroomDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { chatroomEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="socialNetworkBackendApp.chatroom.detail.title">Chatroom</Translate> [<b>{chatroomEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="createdDate">
                <Translate contentKey="socialNetworkBackendApp.chatroom.createdDate">Created Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={chatroomEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="topic">
                <Translate contentKey="socialNetworkBackendApp.chatroom.topic">Topic</Translate>
              </span>
            </dt>
            <dd>{chatroomEntity.topic}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.chatroom.admin">Admin</Translate>
            </dt>
            <dd>{chatroomEntity.admin ? chatroomEntity.admin.displayName : ''}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.chatroom.participant">Participant</Translate>
            </dt>
            <dd>
              {chatroomEntity.participants
                ? chatroomEntity.participants.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.displayName}</a>
                      {i === chatroomEntity.participants.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/chatroom" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/chatroom/${chatroomEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ chatroom }: IRootState) => ({
  chatroomEntity: chatroom.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ChatroomDetail);
