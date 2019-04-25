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
import { IMessage } from 'app/shared/model/message.model';
import { getEntities as getMessages } from 'app/entities/message/message.reducer';
import { getEntity, updateEntity, createEntity, reset } from './location.reducer';
import { ILocation } from 'app/shared/model/location.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILocationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ILocationUpdateState {
  isNew: boolean;
  userId: string;
  messageId: string;
}

export class LocationUpdate extends React.Component<ILocationUpdateProps, ILocationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
      messageId: '0',
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
    this.props.getMessages();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { locationEntity } = this.props;
      const entity = {
        ...locationEntity,
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
    this.props.history.push('/entity/location');
  };

  render() {
    const { locationEntity, profiles, messages, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="socialNetworkBackendApp.location.home.createOrEditLabel">
              <Translate contentKey="socialNetworkBackendApp.location.home.createOrEditLabel">Create or edit a Location</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : locationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="location-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="latitudeLabel" for="latitude">
                    <Translate contentKey="socialNetworkBackendApp.location.latitude">Latitude</Translate>
                  </Label>
                  <AvField
                    id="location-latitude"
                    type="string"
                    className="form-control"
                    name="latitude"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="longitudeLabel" for="longitude">
                    <Translate contentKey="socialNetworkBackendApp.location.longitude">Longitude</Translate>
                  </Label>
                  <AvField
                    id="location-longitude"
                    type="string"
                    className="form-control"
                    name="longitude"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="urlGoogleMapsLabel" for="urlGoogleMaps">
                    <Translate contentKey="socialNetworkBackendApp.location.urlGoogleMaps">Url Google Maps</Translate>
                  </Label>
                  <AvField id="location-urlGoogleMaps" type="text" name="urlGoogleMaps" />
                </AvGroup>
                <AvGroup>
                  <Label id="urlOpenStreetMapLabel" for="urlOpenStreetMap">
                    <Translate contentKey="socialNetworkBackendApp.location.urlOpenStreetMap">Url Open Street Map</Translate>
                  </Label>
                  <AvField id="location-urlOpenStreetMap" type="text" name="urlOpenStreetMap" />
                </AvGroup>
                <AvGroup>
                  <Label id="addressLabel" for="address">
                    <Translate contentKey="socialNetworkBackendApp.location.address">Address</Translate>
                  </Label>
                  <AvField id="location-address" type="text" name="address" />
                </AvGroup>
                <AvGroup>
                  <Label id="postalCodeLabel" for="postalCode">
                    <Translate contentKey="socialNetworkBackendApp.location.postalCode">Postal Code</Translate>
                  </Label>
                  <AvField id="location-postalCode" type="text" name="postalCode" />
                </AvGroup>
                <AvGroup>
                  <Label id="cityLabel" for="city">
                    <Translate contentKey="socialNetworkBackendApp.location.city">City</Translate>
                  </Label>
                  <AvField id="location-city" type="text" name="city" />
                </AvGroup>
                <AvGroup>
                  <Label id="stateProviceLabel" for="stateProvice">
                    <Translate contentKey="socialNetworkBackendApp.location.stateProvice">State Provice</Translate>
                  </Label>
                  <AvField id="location-stateProvice" type="text" name="stateProvice" />
                </AvGroup>
                <AvGroup>
                  <Label id="countyLabel" for="county">
                    <Translate contentKey="socialNetworkBackendApp.location.county">County</Translate>
                  </Label>
                  <AvField id="location-county" type="text" name="county" />
                </AvGroup>
                <AvGroup>
                  <Label id="countryLabel" for="country">
                    <Translate contentKey="socialNetworkBackendApp.location.country">Country</Translate>
                  </Label>
                  <AvField id="location-country" type="text" name="country" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/location" replace color="info">
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
  messages: storeState.message.entities,
  locationEntity: storeState.location.entity,
  loading: storeState.location.loading,
  updating: storeState.location.updating,
  updateSuccess: storeState.location.updateSuccess
});

const mapDispatchToProps = {
  getProfiles,
  getMessages,
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
)(LocationUpdate);
