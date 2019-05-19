import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './direct-message.reducer';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDirectMessageDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DirectMessageDetail extends React.Component<IDirectMessageDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { directMessageEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="socialNetworkBackendApp.directMessage.detail.title">DirectMessage</Translate> [<b>
              {directMessageEntity.id}
            </b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="createdDate">
                <Translate contentKey="socialNetworkBackendApp.directMessage.createdDate">Created Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={directMessageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="message">
                <Translate contentKey="socialNetworkBackendApp.directMessage.message">Message</Translate>
              </span>
            </dt>
            <dd>{directMessageEntity.message}</dd>
            <dt>
              <span id="url">
                <Translate contentKey="socialNetworkBackendApp.directMessage.url">Url</Translate>
              </span>
            </dt>
            <dd>{directMessageEntity.url}</dd>
            <dt>
              <span id="picture">
                <Translate contentKey="socialNetworkBackendApp.directMessage.picture">Picture</Translate>
              </span>
            </dt>
            <dd>
              {directMessageEntity.picture ? (
                <div>
                  <a onClick={openFile(directMessageEntity.pictureContentType, directMessageEntity.picture)}>
                    <img
                      src={`data:${directMessageEntity.pictureContentType};base64,${directMessageEntity.picture}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                  <span>
                    {directMessageEntity.pictureContentType}, {byteSize(directMessageEntity.picture)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.directMessage.sender">Sender</Translate>
            </dt>
            <dd>{directMessageEntity.sender ? directMessageEntity.sender.displayName : ''}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.directMessage.recipient">Recipient</Translate>
            </dt>
            <dd>{directMessageEntity.recipient ? directMessageEntity.recipient.displayName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/direct-message" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/direct-message/${directMessageEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ directMessage }: IRootState) => ({
  directMessageEntity: directMessage.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DirectMessageDetail);
