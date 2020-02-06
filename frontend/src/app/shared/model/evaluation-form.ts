export class EvaluationForm {

    constructor(
        public relevance: number,
        public readability: number,
        public language: number,
        public organization: number,
        public abstract: number,
        public keywords: number,
        public figures: number,
        public conclusion: number,
        public references: number,
        public overallQuality: number,
        public originality: number,
        public contributionValue: number,
        public academicStandards: number,
        public rationale: number,
        public methodology: number,
        public accuracy: number,
        public evidence: number,
        public scientificQuality: number,
        public comment: string
    ) { }

    public getXmlEvaluatinForm(): string {
        return '' +
        `<evaluation_form:evaluation_form xmlns:evaluation_form="http://www.scit.org/schema/evaluation_form"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <evaluation_form:general>
                <evaluation_form:relevance score="${this.relevance}"/>
                <evaluation_form:readability score="${this.readability}"/>
                <evaluation_form:language score="${this.language}"/>
                <evaluation_form:organization score="${this.organization}"/>
                <evaluation_form:abstract score="${this.abstract}"/>
                <evaluation_form:keywords score="${this.keywords}"/>
                <evaluation_form:figures score="${this.figures}"/>
                <evaluation_form:conclusion score="${this.conclusion}"/>
                <evaluation_form:references score="${this.references}"/>
                <evaluation_form:overall_quality score="${this.overallQuality}"/>
            </evaluation_form:general>
            <evaluation_form:technical>
                <evaluation_form:originality score="${this.originality}"/>
                <evaluation_form:contribution_value score="${this.contributionValue}"/>
                <evaluation_form:academic_standards score="${this.academicStandards}"/>
                <evaluation_form:rationale score="${this.rationale}"/>
                <evaluation_form:methodology score="${this.methodology}"/>
                <evaluation_form:accuracy score="${this.accuracy}"/>
                <evaluation_form:evidence score="${this.evidence}"/>
                <evaluation_form:scientific_quality score="${this.scientificQuality}"/>
            </evaluation_form:technical>
            <evaluation_form:recommendation>${this.comment}</evaluation_form:recommendation>
        </evaluation_form:evaluation_form>`

        ;
    }

}
