import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './ethnicity.reducer';
import { IEthnicity } from 'app/shared/model/ethnicity.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEthnicityDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class EthnicityDetail extends React.Component<IEthnicityDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { ethnicityEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="socialNetworkBackendApp.ethnicity.detail.title">Ethnicity</Translate> [<b>{ethnicityEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="ethnicity">
                <Translate contentKey="socialNetworkBackendApp.ethnicity.ethnicity">Ethnicity</Translate>
              </span>
            </dt>
            <dd>{ethnicityEntity.ethnicity}</dd>
          </dl>
          <Button tag={Link} to="/entity/ethnicity" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/ethnicity/${ethnicityEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ ethnicity }: IRootState) => ({
  ethnicityEntity: ethnicity.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EthnicityDetail);
