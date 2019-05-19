import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name={translate('global.menu.entities.main')} id="entity-menu">
    <DropdownItem tag={Link} to="/entity/profile">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.profile" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/gender">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.gender" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/ethnicity">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.ethnicity" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/relationship">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.relationship" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/location">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.location" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/invitation">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.invitation" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/block">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.block" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/chatroom">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.chatroom" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/message">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.message" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/profile">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.profile" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/direct-message">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;<Translate contentKey="global.menu.entities.directMessage" />
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
