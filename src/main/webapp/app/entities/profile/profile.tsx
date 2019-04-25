import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { openFile, byteSize, Translate, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './profile.reducer';
import { IProfile } from 'app/shared/model/profile.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IProfileProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IProfileState = IPaginationBaseState;

export class Profile extends React.Component<IProfileProps, IProfileState> {
  state: IProfileState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { profileList, match } = this.props;
    return (
      <div>
        <h2 id="profile-heading">
          <Translate contentKey="socialNetworkBackendApp.profile.home.title">Profiles</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="socialNetworkBackendApp.profile.home.createLabel">Create new Profile</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('birthDate')}>
                    <Translate contentKey="socialNetworkBackendApp.profile.birthDate">Birth Date</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('picture')}>
                    <Translate contentKey="socialNetworkBackendApp.profile.picture">Picture</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('height')}>
                    <Translate contentKey="socialNetworkBackendApp.profile.height">Height</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('weight')}>
                    <Translate contentKey="socialNetworkBackendApp.profile.weight">Weight</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('unitSystem')}>
                    <Translate contentKey="socialNetworkBackendApp.profile.unitSystem">Unit System</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('aboutMe')}>
                    <Translate contentKey="socialNetworkBackendApp.profile.aboutMe">About Me</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('displayName')}>
                    <Translate contentKey="socialNetworkBackendApp.profile.displayName">Display Name</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('showAge')}>
                    <Translate contentKey="socialNetworkBackendApp.profile.showAge">Show Age</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('banned')}>
                    <Translate contentKey="socialNetworkBackendApp.profile.banned">Banned</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('filterPreferences')}>
                    <Translate contentKey="socialNetworkBackendApp.profile.filterPreferences">Filter Preferences</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="socialNetworkBackendApp.profile.location">Location</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="socialNetworkBackendApp.profile.user">User</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="socialNetworkBackendApp.profile.relationship">Relationship</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="socialNetworkBackendApp.profile.gender">Gender</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="socialNetworkBackendApp.profile.ethnicity">Ethnicity</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {profileList.map((profile, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${profile.id}`} color="link" size="sm">
                        {profile.id}
                      </Button>
                    </td>
                    <td>
                      <TextFormat type="date" value={profile.birthDate} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>
                      {profile.picture ? (
                        <div>
                          <a onClick={openFile(profile.pictureContentType, profile.picture)}>
                            <img src={`data:${profile.pictureContentType};base64,${profile.picture}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                          <span>
                            {profile.pictureContentType}, {byteSize(profile.picture)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>{profile.height}</td>
                    <td>{profile.weight}</td>
                    <td>
                      <Translate contentKey={`socialNetworkBackendApp.UnitSystem.${profile.unitSystem}`} />
                    </td>
                    <td>{profile.aboutMe}</td>
                    <td>{profile.displayName}</td>
                    <td>{profile.showAge ? 'true' : 'false'}</td>
                    <td>{profile.banned ? 'true' : 'false'}</td>
                    <td>{profile.filterPreferences}</td>
                    <td>{profile.location ? <Link to={`location/${profile.location.id}`}>{profile.location.address}</Link> : ''}</td>
                    <td>{profile.user ? profile.user.login : ''}</td>
                    <td>
                      {profile.relationship ? (
                        <Link to={`relationship/${profile.relationship.id}`}>{profile.relationship.status}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>{profile.gender ? <Link to={`gender/${profile.gender.id}`}>{profile.gender.type}</Link> : ''}</td>
                    <td>{profile.ethnicity ? <Link to={`ethnicity/${profile.ethnicity.id}`}>{profile.ethnicity.ethnicity}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${profile.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${profile.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${profile.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ profile }: IRootState) => ({
  profileList: profile.entities,
  totalItems: profile.totalItems,
  links: profile.links,
  entity: profile.entity,
  updateSuccess: profile.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Profile);
