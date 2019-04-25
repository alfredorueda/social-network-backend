import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './block.reducer';
import { IBlock } from 'app/shared/model/block.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IBlockDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class BlockDetail extends React.Component<IBlockDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { blockEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="socialNetworkBackendApp.block.detail.title">Block</Translate> [<b>{blockEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="createdDate">
                <Translate contentKey="socialNetworkBackendApp.block.createdDate">Created Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={blockEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.block.sent">Sent</Translate>
            </dt>
            <dd>{blockEntity.sent ? blockEntity.sent.displayName : ''}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.block.received">Received</Translate>
            </dt>
            <dd>{blockEntity.received ? blockEntity.received.displayName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/block" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/block/${blockEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ block }: IRootState) => ({
  blockEntity: block.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(BlockDetail);
