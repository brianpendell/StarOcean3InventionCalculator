import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './invention.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const InventionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const inventionEntity = useAppSelector(state => state.invention.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="inventionDetailsHeading">
          <Translate contentKey="so3InventionCalculatorApp.invention.detail.title">Invention</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{inventionEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="so3InventionCalculatorApp.invention.name">Name</Translate>
            </span>
          </dt>
          <dd>{inventionEntity.name}</dd>
          <dt>
            <span id="cost">
              <Translate contentKey="so3InventionCalculatorApp.invention.cost">Cost</Translate>
            </span>
          </dt>
          <dd>{inventionEntity.cost}</dd>
          <dt>
            <span id="difficulty">
              <Translate contentKey="so3InventionCalculatorApp.invention.difficulty">Difficulty</Translate>
            </span>
          </dt>
          <dd>{inventionEntity.difficulty}</dd>
          <dt>
            <Translate contentKey="so3InventionCalculatorApp.invention.discipline">Discipline</Translate>
          </dt>
          <dd>{inventionEntity.discipline ? inventionEntity.discipline.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/invention" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/invention/${inventionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InventionDetail;
