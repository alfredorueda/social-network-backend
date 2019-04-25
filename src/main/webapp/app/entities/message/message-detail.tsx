import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './message.reducer';
import { IMessage } from 'app/shared/model/message.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMessageDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MessageDetail extends React.Component<IMessageDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { messageEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="socialNetworkBackendApp.message.detail.title">Message</Translate> [<b>{messageEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="createdDate">
                <Translate contentKey="socialNetworkBackendApp.message.createdDate">Created Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={messageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="message">
                <Translate contentKey="socialNetworkBackendApp.message.message">Message</Translate>
              </span>
            </dt>
            <dd>{messageEntity.message}</dd>
            <dt>
              <span id="url">
                <Translate contentKey="socialNetworkBackendApp.message.url">Url</Translate>
              </span>
            </dt>
            <dd>{messageEntity.url}</dd>
            <dt>
              <span id="picture">
                <Translate contentKey="socialNetworkBackendApp.message.picture">Picture</Translate>
              </span>
            </dt>
            <dd>
              {messageEntity.picture ? (
                <div>
                  <a onClick={openFile(messageEntity.pictureContentType, messageEntity.picture)}>
                    <img src={`data:${messageEntity.pictureContentType};base64,${messageEntity.picture}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {messageEntity.pictureContentType}, {byteSize(messageEntity.picture)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.message.location">Location</Translate>
            </dt>
            <dd>{messageEntity.location ? messageEntity.location.address : ''}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.message.sender">Sender</Translate>
            </dt>
            <dd>{messageEntity.sender ? messageEntity.sender.displayName : ''}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.message.chatroom">Chatroom</Translate>
            </dt>
            <dd>{messageEntity.chatroom ? messageEntity.chatroom.topic : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/message" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/message/${messageEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ message }: IRootState) => ({
  messageEntity: message.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MessageDetail);
