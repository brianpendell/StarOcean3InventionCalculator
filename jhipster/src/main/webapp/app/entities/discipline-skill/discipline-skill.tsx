import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './discipline-skill.reducer';
import { IDisciplineSkill } from 'app/shared/model/discipline-skill.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DisciplineSkill = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const disciplineSkillList = useAppSelector(state => state.disciplineSkill.entities);
  const loading = useAppSelector(state => state.disciplineSkill.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="discipline-skill-heading" data-cy="DisciplineSkillHeading">
        <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.home.title">Discipline Skills</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.home.createLabel">Create new Discipline Skill</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {disciplineSkillList && disciplineSkillList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.skillModifier">Skill Modifier</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.inventor">Inventor</Translate>
                </th>
                <th>
                  <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.discipline">Discipline</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {disciplineSkillList.map((disciplineSkill, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${disciplineSkill.id}`} color="link" size="sm">
                      {disciplineSkill.id}
                    </Button>
                  </td>
                  <td>{disciplineSkill.skillModifier}</td>
                  <td>
                    {disciplineSkill.inventor ? (
                      <Link to={`inventor/${disciplineSkill.inventor.id}`}>{disciplineSkill.inventor.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {disciplineSkill.discipline ? (
                      <Link to={`discipline/${disciplineSkill.discipline.id}`}>{disciplineSkill.discipline.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${disciplineSkill.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${disciplineSkill.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${disciplineSkill.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="so3InventionCalculatorApp.disciplineSkill.home.notFound">No Discipline Skills found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default DisciplineSkill;
