import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './discipline-skill.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DisciplineSkillDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const disciplineSkillEntity = useAppSelector(state => state.disciplineSkill.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="disciplineSkillDetailsHeading">
          <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.detail.title">DisciplineSkill</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{disciplineSkillEntity.id}</dd>
          <dt>
            <span id="skillModifier">
              <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.skillModifier">Skill Modifier</Translate>
            </span>
          </dt>
          <dd>{disciplineSkillEntity.skillModifier}</dd>
          <dt>
            <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.inventor">Inventor</Translate>
          </dt>
          <dd>{disciplineSkillEntity.inventor ? disciplineSkillEntity.inventor.id : ''}</dd>
          <dt>
            <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.discipline">Discipline</Translate>
          </dt>
          <dd>{disciplineSkillEntity.discipline ? disciplineSkillEntity.discipline.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/discipline-skill" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/discipline-skill/${disciplineSkillEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DisciplineSkillDetail;
