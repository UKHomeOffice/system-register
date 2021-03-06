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
        const element = await getElements(testDate, name);
        const expected = `<span class="message" data-testid="modified-on">Last modified: 2 May 1991 by Betty Boop</span>`;
        expect(element).toContainHTML(expected)
    })

    it('renders <strong /> with "Never" text and no name, when no date and no name is supplied', async() =>{
        const element = await getElements(noDate, noName);
        const expected = `<span class="message" data-testid="modified-on">Last modified: <span>Never</span></span>`;
        expect(element).toContainHTML(expected)
    })

    it('renders <span /> with correctly formatted date but no name, if name not supplied', async () => {
       const element = await getElements(testDate, noName);
        const expected = `<span class="message" data-testid="modified-on">Last modified: 2 May 1991</span>`;
        expect(element).toContainHTML(expected)
    })
})

function getElements(date, authorName) {
    const { getByTestId } = render(<ModifiedDetails date={date} author_name={authorName}/>);
    return getByTestId('modified-on')
}