import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './inventor.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const InventorDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const inventorEntity = useAppSelector(state => state.inventor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="inventorDetailsHeading">
          <Translate contentKey="so3InventionCalculatorApp.inventor.detail.title">Inventor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{inventorEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="so3InventionCalculatorApp.inventor.name">Name</Translate>
            </span>
          </dt>
          <dd>{inventorEntity.name}</dd>
          <dt>
            <span id="timeModifer">
              <Translate contentKey="so3InventionCalculatorApp.inventor.timeModifer">Time Modifer</Translate>
            </span>
          </dt>
          <dd>{inventorEntity.timeModifer}</dd>
          <dt>
            <span id="costModifier">
              <Translate contentKey="so3InventionCalculatorApp.inventor.costModifier">Cost Modifier</Translate>
            </span>
          </dt>
          <dd>{inventorEntity.costModifier}</dd>
          <dt>
            <Translate contentKey="so3InventionCalculatorApp.inventor.invention">Invention</Translate>
          </dt>
          <dd>{inventorEntity.invention ? inventorEntity.invention.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/inventor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/inventor/${inventorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InventorDetail;
