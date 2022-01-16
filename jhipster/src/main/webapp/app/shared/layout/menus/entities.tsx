import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/discipline">
      <Translate contentKey="global.menu.entities.discipline" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/discipline-skill">
      <Translate contentKey="global.menu.entities.disciplineSkill" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/inventor">
      <Translate contentKey="global.menu.entities.inventor" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/invention">
      <Translate contentKey="global.menu.entities.invention" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
