import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './discipline.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DisciplineDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const disciplineEntity = useAppSelector(state => state.discipline.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="disciplineDetailsHeading">
          <Translate contentKey="so3InventionCalculatorApp.discipline.detail.title">Discipline</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{disciplineEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="so3InventionCalculatorApp.discipline.name">Name</Translate>
            </span>
          </dt>
          <dd>{disciplineEntity.name}</dd>
          <dt>
            <span id="helperDevice">
              <Translate contentKey="so3InventionCalculatorApp.discipline.helperDevice">Helper Device</Translate>
            </span>
          </dt>
          <dd>{disciplineEntity.helperDevice}</dd>
        </dl>
        <Button tag={Link} to="/discipline" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/discipline/${disciplineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DisciplineDetail;
