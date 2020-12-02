import ModifiedDetails from "../ModifiedDetails";
import React from "react";
import { cleanup, render } from '@testing-library/react'


const testDate = '1991-05-02T01:01:01';
const noDate = '';
const name = 'Betty Boop';
const noName = '';

afterEach(cleanup);

describe('<ModifiedDetails />', () => {
    it('renders <span /> with correctly formatted date and name', async () => {
        const { getByTestId } = render(<ModifiedDetails date={testDate} author_name={name}/>);
        const element = await getByTestId('modified-on');
        const expected = `<span data-testid="modified-on">Last modified: 2 May 1991 by Betty Boop</span>`;
        expect(element).toContainHTML(expected)
    })

    it('renders <strong /> with "Never" text and no name, when no date and no name is supplied', async() =>{
        const { getByTestId } = render(<ModifiedDetails date={noDate} author_name={noName}/> );
        const element = await getByTestId('modified-on');
        const expected = `<span data-testid="modified-on">Last modified: <span>Never</span></span>`;
        expect(element).toContainHTML(expected)
    })

    it('renders <span /> with correctly formatted date but no name, if name not supplied', async () => {
        const { getByTestId } = render(<ModifiedDetails date={testDate} author_name={noName}/>);
        const element = await getByTestId('modified-on');
        const expected = `<span data-testid="modified-on">Last modified: 2 May 1991</span>`;
        expect(element).toContainHTML(expected)
    })
})