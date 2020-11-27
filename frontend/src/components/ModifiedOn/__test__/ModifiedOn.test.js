import ModifiedOn from "../ModifiedOn";
import React from "react";
import { cleanup, render } from '@testing-library/react'


const testDate = '1991-05-02T01:01:01';
const noDate = '';

afterEach(cleanup);

describe('<ModifiedOn />', () => {
    it('renders <span /> with correctly formatted date', async () => {
        const { getByTestId } = render(<ModifiedOn date={testDate}/>);
        const element = await getByTestId('modified-on');
        const expected = `<span data-testid="modified-on">Last modified: 2 May 1991</span>`;
        expect(element).toContainHTML(expected)
    })

    it('renders <strong /> with "Never" text, when no date is supplied', async() =>{
        const { getByTestId } = render(<ModifiedOn date={noDate}/>);
        const element = await getByTestId('modified-on');
        const expected = `<span data-testid="modified-on">Last modified: <span>Never</span></span>`;
        expect(element).toContainHTML(expected)
    })
})