import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './profile.reducer';
import { IProfile } from 'app/shared/model/profile.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProfileDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProfileDetail extends React.Component<IProfileDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { profileEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="socialNetworkBackendApp.profile.detail.title">Profile</Translate> [<b>{profileEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="birthDate">
                <Translate contentKey="socialNetworkBackendApp.profile.birthDate">Birth Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={profileEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="picture">
                <Translate contentKey="socialNetworkBackendApp.profile.picture">Picture</Translate>
              </span>
            </dt>
            <dd>
              {profileEntity.picture ? (
                <div>
                  <a onClick={openFile(profileEntity.pictureContentType, profileEntity.picture)}>
                    <img src={`data:${profileEntity.pictureContentType};base64,${profileEntity.picture}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {profileEntity.pictureContentType}, {byteSize(profileEntity.picture)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="height">
                <Translate contentKey="socialNetworkBackendApp.profile.height">Height</Translate>
              </span>
            </dt>
            <dd>{profileEntity.height}</dd>
            <dt>
              <span id="weight">
                <Translate contentKey="socialNetworkBackendApp.profile.weight">Weight</Translate>
              </span>
            </dt>
            <dd>{profileEntity.weight}</dd>
            <dt>
              <span id="unitSystem">
                <Translate contentKey="socialNetworkBackendApp.profile.unitSystem">Unit System</Translate>
              </span>
            </dt>
            <dd>{profileEntity.unitSystem}</dd>
            <dt>
              <span id="aboutMe">
                <Translate contentKey="socialNetworkBackendApp.profile.aboutMe">About Me</Translate>
              </span>
            </dt>
            <dd>{profileEntity.aboutMe}</dd>
            <dt>
              <span id="displayName">
                <Translate contentKey="socialNetworkBackendApp.profile.displayName">Display Name</Translate>
              </span>
            </dt>
            <dd>{profileEntity.displayName}</dd>
            <dt>
              <span id="showAge">
                <Translate contentKey="socialNetworkBackendApp.profile.showAge">Show Age</Translate>
              </span>
            </dt>
            <dd>{profileEntity.showAge ? 'true' : 'false'}</dd>
            <dt>
              <span id="banned">
                <Translate contentKey="socialNetworkBackendApp.profile.banned">Banned</Translate>
              </span>
            </dt>
            <dd>{profileEntity.banned ? 'true' : 'false'}</dd>
            <dt>
              <span id="filterPreferences">
                <Translate contentKey="socialNetworkBackendApp.profile.filterPreferences">Filter Preferences</Translate>
              </span>
            </dt>
            <dd>{profileEntity.filterPreferences}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.profile.location">Location</Translate>
            </dt>
            <dd>{profileEntity.location ? profileEntity.location.address : ''}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.profile.user">User</Translate>
            </dt>
            <dd>{profileEntity.user ? profileEntity.user.login : ''}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.profile.relationship">Relationship</Translate>
            </dt>
            <dd>{profileEntity.relationship ? profileEntity.relationship.status : ''}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.profile.gender">Gender</Translate>
            </dt>
            <dd>{profileEntity.gender ? profileEntity.gender.type : ''}</dd>
            <dt>
              <Translate contentKey="socialNetworkBackendApp.profile.ethnicity">Ethnicity</Translate>
            </dt>
            <dd>{profileEntity.ethnicity ? profileEntity.ethnicity.ethnicity : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/profile" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/profile/${profileEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ profile }: IRootState) => ({
  profileEntity: profile.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProfileDetail);
