package com.choranet.commun


class PhaseJob {
    
    def phaseService
    
    static triggers = {
        cron name: 'phaseTrigger', startDelay:3600000, cronExpression: '0 0 02 * * ?'
        //cron name: 'phaseTrigger', startDelay:60000, cronExpression: '0 * 11 * * ?' // test execution apres 1min du demrage systeme, execution chaque minitue 
    }
    def group = "StatutGroup"

    def execute(){
        phaseService.updatePhaseExpiree()
    }
}
