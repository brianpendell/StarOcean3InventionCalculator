import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './inventor.reducer';
import { IInventor } from 'app/shared/model/inventor.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Inventor = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const inventorList = useAppSelector(state => state.inventor.entities);
  const loading = useAppSelector(state => state.inventor.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="inventor-heading" data-cy="InventorHeading">
        <Translate contentKey="so3InventionCalculatorApp.inventor.home.title">Inventors</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="so3InventionCalculatorApp.inventor.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="so3InventionCalculatorApp.inventor.home.createLabel">Create new Inventor</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {inventorList && inventorList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.inventor.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.inventor.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.inventor.timeModifer">Time Modifer</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.inventor.costModifier">Cost Modifier</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.inventor.invention">Invention</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {inventorList.map((inventor, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${inventor.id}`} color="link" size="sm">
                      {inventor.id}
                    </Button>
                  </td>
                  <td>{inventor.name}</td>
                  <td>{inventor.timeModifer}</td>
                  <td>{inventor.costModifier}</td>
                  <td>{inventor.invention ? <Link to={`invention/${inventor.invention.id}`}>{inventor.invention.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${inventor.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${inventor.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${inventor.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="so3InventionCalculatorApp.inventor.home.notFound">No Inventors found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Inventor;
