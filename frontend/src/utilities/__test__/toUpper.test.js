import toUpper from "../toUpper";

const testText = 'sneaky-Fig_jumps123 &£@';

describe('toUpper()' , () => {
    it('upperises text correctly', () => {
        const upperisedText = toUpper(testText);
        expect(upperisedText).toEqual('SNEAKY-FIG JUMPS123 &£@');
    })
})