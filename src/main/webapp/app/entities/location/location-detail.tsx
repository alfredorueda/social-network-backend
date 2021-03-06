import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './location.reducer';
import { ILocation } from 'app/shared/model/location.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILocationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class LocationDetail extends React.Component<ILocationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { locationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="socialNetworkBackendApp.location.detail.title">Location</Translate> [<b>{locationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="latitude">
                <Translate contentKey="socialNetworkBackendApp.location.latitude">Latitude</Translate>
              </span>
            </dt>
            <dd>{locationEntity.latitude}</dd>
            <dt>
              <span id="longitude">
                <Translate contentKey="socialNetworkBackendApp.location.longitude">Longitude</Translate>
              </span>
            </dt>
            <dd>{locationEntity.longitude}</dd>
            <dt>
              <span id="urlGoogleMaps">
                <Translate contentKey="socialNetworkBackendApp.location.urlGoogleMaps">Url Google Maps</Translate>
              </span>
            </dt>
            <dd>{locationEntity.urlGoogleMaps}</dd>
            <dt>
              <span id="urlOpenStreetMap">
                <Translate contentKey="socialNetworkBackendApp.location.urlOpenStreetMap">Url Open Street Map</Translate>
              </span>
            </dt>
            <dd>{locationEntity.urlOpenStreetMap}</dd>
            <dt>
              <span id="address">
                <Translate contentKey="socialNetworkBackendApp.location.address">Address</Translate>
              </span>
            </dt>
            <dd>{locationEntity.address}</dd>
            <dt>
              <span id="postalCode">
                <Translate contentKey="socialNetworkBackendApp.location.postalCode">Postal Code</Translate>
              </span>
            </dt>
            <dd>{locationEntity.postalCode}</dd>
            <dt>
              <span id="city">
                <Translate contentKey="socialNetworkBackendApp.location.city">City</Translate>
              </span>
            </dt>
            <dd>{locationEntity.city}</dd>
            <dt>
              <span id="stateProvice">
                <Translate contentKey="socialNetworkBackendApp.location.stateProvice">State Provice</Translate>
              </span>
            </dt>
            <dd>{locationEntity.stateProvice}</dd>
            <dt>
              <span id="county">
                <Translate contentKey="socialNetworkBackendApp.location.county">County</Translate>
              </span>
            </dt>
            <dd>{locationEntity.county}</dd>
            <dt>
              <span id="country">
                <Translate contentKey="socialNetworkBackendApp.location.country">Country</Translate>
              </span>
            </dt>
            <dd>{locationEntity.country}</dd>
          </dl>
          <Button tag={Link} to="/entity/location" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/location/${locationEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ location }: IRootState) => ({
  locationEntity: location.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LocationDetail);
